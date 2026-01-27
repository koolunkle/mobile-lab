import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:todo/domain/usecases/load_todo_collections.dart';
import 'package:todo/presentation/core/page_config.dart';
import 'package:todo/presentation/pages/overview/bloc/todo_overview_cubit.dart';
import 'package:todo/presentation/pages/overview/viewstates/todo_overview_error.dart';
import 'package:todo/presentation/pages/overview/viewstates/todo_overview_loaded.dart';
import 'package:todo/presentation/pages/overview/viewstates/todo_overview_loading.dart';

class OverViewPageProvider extends StatelessWidget {
  const OverViewPageProvider({super.key});

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create:
          (context) => ToDoOverviewCubit(
            loadToDoCollections: LoadToDoCollections(
              todoRepository: RepositoryProvider.of(context),
            ),
          )..readToDoCollections(),
      child: OverviewPage(),
    );
  }
}

class OverviewPage extends StatelessWidget {
  const OverviewPage({super.key});

  static const pageConfig = PageConfig(
    icon: Icons.work_history_rounded,
    name: 'overview',
    child: OverViewPageProvider(),
  );

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.tealAccent,
      child: BlocBuilder<ToDoOverviewCubit, ToDoOverviewCubitState>(
        builder: (context, state) {
          if (state is ToDoOverviewCubitLoadingState) {
            return ToDoOverviewLoading();
          } else if (state is ToDoOverviewCubitLoadedState) {
            return ToDoOverviewLoaded(collections: state.collections);
          } else {
            return const ToDoOverviewError();
          }
        },
      ),
    );
  }
}
