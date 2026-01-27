import 'package:equatable/equatable.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:todo/core/usecase.dart';
import 'package:todo/domain/entities/unique_id.dart';
import 'package:todo/domain/usecases/load_todo_entry_ids_for_collection.dart';

part 'todo_detail_cubit_state.dart';

class ToDoDetailCubit extends Cubit<ToDoDetailCubitState> {
  ToDoDetailCubit({
    this.collectionId,
    required this.loadToDoEntryIdsForCollection,
  }) : super(ToDoDetailCubitLoadingState());

  final CollectionId? collectionId;
  final LoadToDoEntryIdsForCollection loadToDoEntryIdsForCollection;

  Future<void> fetch() async {
    emit(ToDoDetailCubitLoadingState());
    if (collectionId != null) {
      try {
        final entryIds = await loadToDoEntryIdsForCollection.call(
          CollectionIdParam(collectionId: collectionId!),
        );

        if (entryIds.isLeft) {
          emit(ToDoDetailCubitErrorState());
        } else {
          emit(ToDoDetailCubitLoadedState(entryIds: entryIds.right));
        }
      } on Exception {
        emit(ToDoDetailCubitErrorState());
      }
    } else {
      emit(ToDoDetailCubitErrorState());
    }
  }
}
