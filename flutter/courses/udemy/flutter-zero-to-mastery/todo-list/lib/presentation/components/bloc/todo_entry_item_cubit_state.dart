part of 'todo_entry_item_cubit.dart';

abstract class ToDoEntryItemCubitState extends Equatable {
  const ToDoEntryItemCubitState();

  @override
  List<Object?> get props => [];
}

class ToDoEntryItemLoadingState extends ToDoEntryItemCubitState {}

class ToDoEntryItemErrorState extends ToDoEntryItemCubitState {}

class ToDoEntryItemLoadedState extends ToDoEntryItemCubitState {
  const ToDoEntryItemLoadedState({required this.toDoEntry});

  final ToDoEntry toDoEntry;

  @override
  List<Object?> get props => [toDoEntry];
}
