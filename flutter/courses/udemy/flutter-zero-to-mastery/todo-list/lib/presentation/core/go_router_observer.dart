import 'package:flutter/material.dart';

class GoRouterObserver extends NavigatorObserver {
  @override
  void didChangeTop(Route topRoute, Route? previousTopRoute) {
    debugPrint(
      'didChangeTop: ${topRoute.settings.name} previousRoute: ${previousTopRoute?.settings.name}',
    );
  }

  @override
  void didPop(Route route, Route? previousRoute) {
    debugPrint(
      'didPop: ${route.settings.name} previousRoute: ${previousRoute?.settings.name}',
    );
  }

  @override
  void didPush(Route route, Route? previousRoute) {
    debugPrint(
      'didPush: ${route.settings.name} previousRoute: ${previousRoute?.settings.name}',
    );
  }

  @override
  void didRemove(Route route, Route? previousRoute) {
    debugPrint(
      'didRemove: ${route.settings.name} previousRoute: ${previousRoute?.settings.name}',
    );
  }

  @override
  void didReplace({Route? newRoute, Route? oldRoute}) {
    debugPrint(
      'didReplace: ${newRoute?.settings.name} previousRoute: ${oldRoute?.settings.name}',
    );
  }

  @override
  void didStartUserGesture(Route route, Route? previousRoute) {
    debugPrint(
      'didStartUserGesture: ${route.settings.name} previousRoute: ${previousRoute?.settings.name}',
    );
  }

  @override
  void didStopUserGesture() {
    debugPrint('didStopUserGesture');
  }
}
