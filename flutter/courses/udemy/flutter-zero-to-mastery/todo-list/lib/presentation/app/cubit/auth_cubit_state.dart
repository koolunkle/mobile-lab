part of 'auth_cubit.dart';

@immutable
abstract class AuthCubitState {}

class AuthCubitInitial extends AuthCubitState {
  AuthCubitInitial({required this.isLoggedIn, this.userId});

  final bool isLoggedIn;
  final String? userId;
}
