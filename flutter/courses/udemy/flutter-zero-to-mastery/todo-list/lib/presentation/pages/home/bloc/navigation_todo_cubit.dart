import 'package:equatable/equatable.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:todo/domain/entities/unique_id.dart';

part 'navigation_todo_cubit_state.dart';

class NavigationToDoCubit extends Cubit<NavigationToDoCubitState> {
  NavigationToDoCubit() : super(const NavigationToDoCubitState());

  void selectedToDoCollectionChanged(CollectionId collectionId) {
    debugPrint(collectionId.value);
    emit(NavigationToDoCubitState(selectedCollectionId: collectionId));
  }

  void secondBodyHasChanged({required bool isSecondBodyDisplayed}) {
    emit(
      NavigationToDoCubitState(
        isSecondBodyDisplayed: isSecondBodyDisplayed,
        selectedCollectionId: state.selectedCollectionId,
      ),
    );
  }
}
