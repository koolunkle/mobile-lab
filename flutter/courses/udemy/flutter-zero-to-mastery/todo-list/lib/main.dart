import 'package:easy_localization/easy_localization.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_crashlytics/firebase_crashlytics.dart';
import 'package:firebase_ui_auth/firebase_ui_auth.dart' as ui_auth;
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:todo/data/local/hive_local_datasource.dart';
import 'package:todo/data/remote/firestore_remote_data_source.dart';
import 'package:todo/data/repositories/todo_repository_mixed.dart';
import 'package:todo/domain/repositories/todo_repository.dart';
import 'package:todo/firebase_options.dart';
import 'package:todo/presentation/app/app.dart';
import 'package:todo/presentation/app/cubit/auth_cubit.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await EasyLocalization.ensureInitialized();

  if (!kIsWeb || !kDebugMode) {
    FlutterError.onError = (errorDetails) {
      FirebaseCrashlytics.instance.recordFlutterFatalError(errorDetails);
    };

    // Pass all uncaught asynchronous errors that aren't handled by the Flutter framework
    PlatformDispatcher.instance.onError = (error, stack) {
      FirebaseCrashlytics.instance.recordError(error, stack, fatal: true);
      return true;
    };
  }

  GoRouter.optionURLReflectsImperativeAPIs = true;

  await Firebase.initializeApp(options: DefaultFirebaseOptions.currentPlatform);

  ui_auth.FirebaseUIAuth.configureProviders([ui_auth.PhoneAuthProvider()]);

  final localDataSource = HiveLocalDataSource();
  await localDataSource.init();

  final remoteDataSource = FirestoreRemoteDataSource();

  final authCubit = AuthCubit();

  FirebaseAuth.instance.authStateChanges().listen((user) {
    debugPrint('user: $user');
    authCubit.authStateChanged(user: user);
  });

  runApp(
    EasyLocalization(
      useOnlyLangCode: true,
      supportedLocales: [Locale('en', 'US'), Locale('kr', 'KR')],
      path: 'assets/translations',
      fallbackLocale: Locale('en', 'US'),
      // startLocale: Locale('kr', 'KR'),
      child: RepositoryProvider<ToDoRepository>(
        create:
            (context) => ToDoRepositoryMixed(
              remoteSource: remoteDataSource,
              localDataSource: localDataSource,
            ),
        child: BlocProvider(create: (context) => authCubit, child: const App()),
      ),
    ),
  );
}
