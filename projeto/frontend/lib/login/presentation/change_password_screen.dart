import 'package:flutter/material.dart';
import 'package:adc_handson_session/login/application/auth.dart';
import 'package:adc_handson_session/login/presentation/main_page.dart';

class ChangePasswordScreen extends StatefulWidget {
  const ChangePasswordScreen({super.key});

  @override
  State<ChangePasswordScreen> createState() => _ChangePasswordScreenState();
}

class _ChangePasswordScreenState extends State<ChangePasswordScreen> {
  late TextEditingController oldPasswordController;
  late TextEditingController newPasswordController;
  late TextEditingController confirmPasswordController;
  late ScrollController scrollController;

  @override
  void initState() {
    oldPasswordController = TextEditingController();
    newPasswordController = TextEditingController();
    confirmPasswordController = TextEditingController();
    scrollController = ScrollController();

    super.initState();
  }

  void changePasswordButtonPressed(String oldPassword, String newPassword, String confirmation) async {

  if(oldPasswordController.text.isEmpty || newPasswordController.text.isEmpty || confirmPasswordController.text.isEmpty) {
    showDialog(
      context: context,
      builder: (context) {
        return const AlertDialog(
          content: Text("Missing required fields!"),
        );
      },
    );
    return;
  }
  
  bool result = await Authentication.changePassword(oldPassword, newPassword, confirmation);

  if (result) {

      Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => const MainScreen()),
      );

    String success = "Password changed succesfully!";
    showDialog(context: context,
     builder: (context) {
       return AlertDialog(
         content: Text(success),
       );},);
  } else {
      String message = await Authentication.getMessage();
      // Wrong credentials
      showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            content: Text(message),
          );
        },
      );
    }

  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Scrollbar(
        controller: scrollController,
        child: SingleChildScrollView(
          controller: scrollController,
      child:  Column(
      children: [
        Container(
          padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
          child: TextField(
            controller: oldPasswordController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'Old password*',
            ),
          ),
        ),
        Container(
          padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
          child: TextField(
            controller: newPasswordController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'New password*',
            ),
          ),
        ),
        Container(
          padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
          child: TextField(
            controller: confirmPasswordController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'Confirmation*',
            ),
          ),
        ),
        Container(
            height: 80,
            padding: const EdgeInsets.all(20),
            child: ElevatedButton(
              style: ElevatedButton.styleFrom(
                minimumSize: const Size.fromHeight(50),
              ),
              child: const Text('Proceed!'),
              onPressed: () => changePasswordButtonPressed(
                  oldPasswordController.text, newPasswordController.text, confirmPasswordController.text),
            )),
      ],
    ))));
  }
}
