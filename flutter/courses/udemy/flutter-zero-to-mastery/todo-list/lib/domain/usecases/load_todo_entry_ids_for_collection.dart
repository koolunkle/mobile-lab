import 'package:either_dart/either.dart';
import 'package:todo/core/usecase.dart';
import 'package:todo/domain/entities/unique_id.dart';
import 'package:todo/domain/failures/failures.dart';
import 'package:todo/domain/repositories/todo_repository.dart';

class LoadToDoEntryIdsForCollection
    implements UseCase<List<EntryId>, CollectionIdParam> {
  const LoadToDoEntryIdsForCollection({required this.toDoRepository});

  final ToDoRepository toDoRepository;

  @override
  Future<Either<Failure, List<EntryId>>> call(params) async {
    try {
      final loadedIds = toDoRepository.readToDoEntryIds(params.collectionId);
      return loadedIds.fold((left) => Left(left), (right) => Right(right));
    } on Exception catch (e) {
      return Left(ServerFailure(stackTrace: e.toString()));
    }
  }
}
