import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:todo/data/repositories/todo_repository_mock.dart';
import 'package:todo/domain/repositories/todo_repository.dart';
import 'package:todo/presentation/app/app.dart';

void main() {
  runApp(
    RepositoryProvider<ToDoRepository>(
      create: (context) => ToDoRepositoryMock(),
      child: const App(),
    ),
  );
}
