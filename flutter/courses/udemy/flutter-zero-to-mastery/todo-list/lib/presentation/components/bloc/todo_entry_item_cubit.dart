import 'package:equatable/equatable.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:todo/core/usecase.dart';
import 'package:todo/domain/entities/todo_entry.dart';
import 'package:todo/domain/entities/unique_id.dart';
import 'package:todo/domain/usecases/load_todo_entry.dart';
import 'package:todo/domain/usecases/update_todo_entry.dart';

part 'todo_entry_item_cubit_state.dart';

class ToDoEntryItemCubit extends Cubit<ToDoEntryItemCubitState> {
  ToDoEntryItemCubit({
    required this.entryId,
    required this.collectionId,
    required this.loadToDoEntry,
    required this.uploadToDoEntry,
  }) : super(ToDoEntryItemLoadingState());

  final EntryId entryId;
  final CollectionId collectionId;
  final LoadToDoEntry loadToDoEntry;
  final UpdateToDoEntry uploadToDoEntry;

  Future<void> fetch() async {
    emit(ToDoEntryItemLoadingState());
    try {
      final entry = await loadToDoEntry.call(
        ToDoEntryIdsParam(collectionId: collectionId, entryId: entryId),
      );
      return entry.fold(
        (left) => emit(ToDoEntryItemErrorState()),
        (right) => emit(ToDoEntryItemLoadedState(toDoEntry: right)),
      );
    } on Exception {
      emit(ToDoEntryItemErrorState());
    }
  }

  Future<void> update() async {
    try {
      if (state is ToDoEntryItemLoadedState) {
        final currentEntry = (state as ToDoEntryItemLoadedState).toDoEntry;

        final entryToUpdate = currentEntry.copyWith(
          isDone: !currentEntry.isDone,
        );

        final updatedEntry = await uploadToDoEntry.call(
          ToDoEntryParams(collectionId: collectionId, entry: entryToUpdate),
        );
        return updatedEntry.fold(
          (left) => emit(ToDoEntryItemErrorState()),
          (right) => emit(ToDoEntryItemLoadedState(toDoEntry: right)),
        );
      }
    } on Exception {
      emit(ToDoEntryItemErrorState());
    }
  }
}
