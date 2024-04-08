import 'package:adc_handson_session/login/application/auth.dart';
import 'package:adc_handson_session/login/presentation/login_screen.dart';
import 'package:adc_handson_session/login/presentation/grant_role_screen.dart';
import 'package:adc_handson_session/login/presentation/grant_state_screen.dart';
import 'package:adc_handson_session/login/presentation/remove_user_screen.dart';
import 'package:adc_handson_session/login/presentation/modify_attrib_screen.dart';  
import 'package:adc_handson_session/login/presentation/change_password_screen.dart';
import 'package:adc_handson_session/login/presentation/show_control_screen.dart';
import 'package:adc_handson_session/login/presentation/list_users_screen.dart';
import 'package:flutter/material.dart';


class MainScreen extends StatefulWidget {
  const MainScreen({super.key});

  @override
  State<MainScreen> createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  late ScrollController scrollController;

  @override
  void initState() {
    scrollController = ScrollController();
    super.initState();
  }

  void changeRoleButtonPressed() {
    Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => const GrantRoleScreen()),
      );
  }

  void changeStateButtonPressed() {
    Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => const GrantStateScreen()),
      );
  }

  void removeClientButtonPressed() {
    Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => const RemoveUserScreen()),
      );
  }

  void listUsersButtonPressed() async {

      Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => const ListUsersScreen()),
      );

      String success = "Users listed succesfully!";
      showDialog(context: context,
       builder: (context) {
         return AlertDialog(
           content: Text(success),
         );},);
  }

  void modifyAttribButtonPressed() {
    Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => const ModifyAttribScreen()),
      );
  }

  void changePasswordButtonPressed() {
    Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => const ChangePasswordScreen()),
      );
  }

  void showControlButtonPressed() async {

      Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => const ShowControlsScreen()),
      );

      String success = "Controls listed succesfully!";
      showDialog(context: context,
       builder: (context) {
         return AlertDialog(
           content: Text(success),
         );},);
  }

  void logoutButtonPressed() {
    Authentication.logout();

    Navigator.of(context, rootNavigator: true)
    .pushAndRemoveUntil(
      MaterialPageRoute(
        builder: (BuildContext context) {
          return const LoginScreen();
        },
      ),
      (_) => false,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Welcome!'),
        ),
        body:
          Scrollbar(
        controller: scrollController,
        child: SingleChildScrollView(
          controller: scrollController,
      child: 
          Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [

            Container(
              padding: const EdgeInsets.fromLTRB(20, 20, 20, 70),
              child: ElevatedButton(
            onPressed: () => changeRoleButtonPressed(),
            child: const Text('Change a clients role!'),
          ),),

            Container(
              padding: const EdgeInsets.fromLTRB(20, 20, 20, 70),
              child: ElevatedButton(
            onPressed: () => changeStateButtonPressed(),
            child: const Text('Change a clients state!'),
          ),),

            Container(
              padding: const EdgeInsets.fromLTRB(20, 20, 20, 70),
              child: ElevatedButton(
            onPressed: () => removeClientButtonPressed(),
            child: const Text('Remove a clients account!'),
          )),

            Container(
              padding: const EdgeInsets.fromLTRB(20, 20, 20, 70),
              child: ElevatedButton(
            onPressed: () => listUsersButtonPressed(),
            child: const Text('List clients accounts!'),
          )),

          Container(
              padding: const EdgeInsets.fromLTRB(20, 20, 20, 70),
              child: ElevatedButton(
            onPressed: () => modifyAttribButtonPressed(),
            child: const Text('Modify a clients account attributes!'),
          )),

          Container(
              padding: const EdgeInsets.fromLTRB(20, 20, 20, 70),
              child: ElevatedButton(
            onPressed: () => changePasswordButtonPressed(),
            child: const Text('Change your password!'),
          )),

          Container(
              padding: const EdgeInsets.fromLTRB(20, 20, 20, 70),
              child: ElevatedButton(
            onPressed: () => showControlButtonPressed(),
            child: const Text('Show Login and Control elements!'),
          )),

          Container(
              padding: const EdgeInsets.fromLTRB(20, 20, 20, 70),
              child: ElevatedButton(
            onPressed: () => logoutButtonPressed(),
            child: const Text('Logout!'),
          )),

                    // By default, show a loading spinner.
                    //return const CircularProgressIndicator();)
            ]))));
  }
}