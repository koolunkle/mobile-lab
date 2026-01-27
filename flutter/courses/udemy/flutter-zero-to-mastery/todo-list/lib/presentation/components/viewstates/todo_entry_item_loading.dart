import 'package:flutter/material.dart';
import 'package:shimmer/shimmer.dart';

class ToDoEntryItemLoading extends StatelessWidget {
  const ToDoEntryItemLoading({super.key});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Shimmer.fromColors(
        baseColor: Theme.of(context).colorScheme.onSurface,
        highlightColor: Theme.of(context).colorScheme.onSurfaceVariant,
        child: const Text('Loading'),
      ),
    );
  }
}
