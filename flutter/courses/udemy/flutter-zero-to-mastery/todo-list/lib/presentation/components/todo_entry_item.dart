import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:todo/domain/entities/unique_id.dart';
import 'package:todo/domain/repositories/todo_repository.dart';
import 'package:todo/domain/usecases/load_todo_entry.dart';
import 'package:todo/domain/usecases/update_todo_entry.dart';
import 'package:todo/presentation/components/bloc/todo_entry_item_cubit.dart';
import 'package:todo/presentation/components/viewstates/todo_entry_item_error.dart';
import 'package:todo/presentation/components/viewstates/todo_entry_item_loaded.dart';
import 'package:todo/presentation/components/viewstates/todo_entry_item_loading.dart';

class ToDoEntryItemProvider extends StatelessWidget {
  const ToDoEntryItemProvider({
    super.key,
    required this.entryId,
    required this.collectionId,
  });

  final EntryId entryId;
  final CollectionId collectionId;

  @override
  Widget build(BuildContext context) {
    return BlocProvider<ToDoEntryItemCubit>(
      create:
          (context) => ToDoEntryItemCubit(
            entryId: entryId,
            collectionId: collectionId,
            loadToDoEntry: LoadToDoEntry(
              toDoRepository: RepositoryProvider.of<ToDoRepository>(context),
            ),
            uploadToDoEntry: UpdateToDoEntry(
              toDoRepository: RepositoryProvider.of<ToDoRepository>(context),
            ),
          )..fetch(),
      child: ToDoEntryItem(),
    );
  }
}

class ToDoEntryItem extends StatelessWidget {
  const ToDoEntryItem({super.key});

  @override
  Widget build(BuildContext context) {
    return BlocBuilder<ToDoEntryItemCubit, ToDoEntryItemCubitState>(
      builder: (context, state) {
        if (state is ToDoEntryItemLoadingState) {
          return const ToDoEntryItemLoading();
        } else if (state is ToDoEntryItemLoadedState) {
          return ToDoEntryItemLoaded(
            entryItem: state.toDoEntry,
            onChanged: (value) => context.read<ToDoEntryItemCubit>().update(),
          );
        } else {
          return const ToDoEntryItemError();
        }
      },
    );
  }
}
