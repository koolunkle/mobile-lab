import 'package:flutter/material.dart';
import 'package:flutter_adaptive_scaffold/flutter_adaptive_scaffold.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:todo/presentation/core/page_config.dart';
import 'package:todo/presentation/pages/dashboard/dashboard_page.dart';
import 'package:todo/presentation/pages/detail/todo_detail_page.dart';
import 'package:todo/presentation/pages/home/bloc/navigation_todo_cubit.dart';
import 'package:todo/presentation/pages/home/components/login_button.dart';
import 'package:todo/presentation/pages/overview/overview_page.dart';
import 'package:todo/presentation/pages/settings/settings_page.dart';

class HomePageProvider extends StatelessWidget {
  const HomePageProvider({super.key, required this.tab});

  final String tab;

  @override
  Widget build(BuildContext context) {
    return BlocProvider<NavigationToDoCubit>(
      create: (_) => NavigationToDoCubit(),
      child: HomePage(tab: tab),
    );
  }
}

class HomePage extends StatefulWidget {
  HomePage({super.key, required String tab})
    : index = tabs.indexWhere((element) => element.name == tab);

  static const PageConfig pageConfig = PageConfig(
    icon: Icons.home_rounded,
    name: 'home',
  );

  final int index;

  // list of all tabs that should be displayed inside our navigation bar
  static const tabs = [DashboardPage.pageConfig, OverviewPage.pageConfig];

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  final destinations =
      HomePage.tabs
          .map(
            (page) =>
                NavigationDestination(icon: Icon(page.icon), label: page.name),
          )
          .toList();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: BlocListener<NavigationToDoCubit, NavigationToDoCubitState>(
          listenWhen:
              (previous, current) =>
                  previous.isSecondBodyDisplayed !=
                  current.isSecondBodyDisplayed,
          listener: (context, state) {
            if (context.canPop() && (state.isSecondBodyDisplayed ?? false)) {
              context.pop();
            }
          },
          child: AdaptiveLayout(
            internalAnimations: false,
            primaryNavigation: SlotLayout(
              config: <Breakpoint, SlotLayoutConfig>{
                Breakpoints.mediumAndUp: SlotLayout.from(
                  key: Key('primary-navigation-medium'),
                  builder:
                      (context) => AdaptiveScaffold.standardNavigationRail(
                        leading: LoginButton(),
                        trailing: IconButton(
                          onPressed: () {
                            context.pushNamed(SettingsPage.pageConfig.name);
                          },
                          icon: Icon(SettingsPage.pageConfig.icon),
                        ),
                        selectedIndex: widget.index,
                        onDestinationSelected:
                            (index) =>
                                _tabOnNavigationDestination(context, index),
                        destinations:
                            destinations
                                .map(
                                  (element) =>
                                      AdaptiveScaffold.toRailDestination(
                                        element,
                                      ),
                                )
                                .toList(),
                      ),
                ),
              },
            ),
            topNavigation: SlotLayout(
              config: <Breakpoint, SlotLayoutConfig>{
                Breakpoints.small: SlotLayout.from(
                  key: Key('top-navigation-small'),
                  builder:
                      (context) => Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          IconButton(
                            onPressed:
                                () => context.goNamed(
                                  SettingsPage.pageConfig.name,
                                ),
                            icon: Icon(Icons.settings),
                          ),
                          LoginButton(),
                        ],
                      ),
                ),
              },
            ),
            bottomNavigation: SlotLayout(
              config: <Breakpoint, SlotLayoutConfig>{
                Breakpoints.small: SlotLayout.from(
                  key: Key('bottom-navigation-small'),
                  builder:
                      (context) => AdaptiveScaffold.standardBottomNavigationBar(
                        currentIndex: widget.index,
                        destinations: destinations,
                        onDestinationSelected:
                            (index) =>
                                _tabOnNavigationDestination(context, index),
                      ),
                ),
              },
            ),
            body: SlotLayout(
              config: <Breakpoint, SlotLayoutConfig>{
                Breakpoints.smallAndUp: SlotLayout.from(
                  key: Key('primary-body-small'),
                  builder: (context) => HomePage.tabs[widget.index].child,
                ),
              },
            ),
            secondaryBody: SlotLayout(
              config: <Breakpoint, SlotLayoutConfig>{
                Breakpoints.mediumAndUp: SlotLayout.from(
                  key: Key('secondary-body-medium'),
                  builder:
                      widget.index != 1
                          ? null
                          : (_) => BlocBuilder<
                            NavigationToDoCubit,
                            NavigationToDoCubitState
                          >(
                            builder: (context, state) {
                              final selectedId = state.selectedCollectionId;
                              final isSecondBodyDisplayed = Breakpoints
                                  .mediumAndUp
                                  .isActive(context);

                              context
                                  .read<NavigationToDoCubit>()
                                  .secondBodyHasChanged(
                                    isSecondBodyDisplayed:
                                        isSecondBodyDisplayed,
                                  );

                              if (selectedId == null) {
                                return SizedBox.shrink();
                              } else {
                                return ToDoDetailPageProvider(
                                  key: Key(selectedId.value),
                                  collectionId: selectedId,
                                );
                              }
                            },
                          ),
                ),
              },
            ),
          ),
        ),
      ),
    );
  }

  void _tabOnNavigationDestination(BuildContext context, int index) =>
      context.goNamed(
        HomePage.pageConfig.name,
        pathParameters: {'tab': HomePage.tabs[index].name},
      );
}
