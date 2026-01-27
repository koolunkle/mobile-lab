part of 'navigation_todo_cubit.dart';

class NavigationToDoCubitState extends Equatable {
  const NavigationToDoCubitState({
    this.selectedCollectionId,
    this.isSecondBodyDisplayed,
  });

  final CollectionId? selectedCollectionId;
  final bool? isSecondBodyDisplayed;

  @override
  List<Object?> get props => [selectedCollectionId, isSecondBodyDisplayed];
}
