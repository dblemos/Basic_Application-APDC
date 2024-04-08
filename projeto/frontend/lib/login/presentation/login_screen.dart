import 'package:adc_handson_session/login/presentation/register_screen.dart';
import 'package:flutter/material.dart';
import 'package:adc_handson_session/login/application/auth.dart';
import 'package:adc_handson_session/login/presentation/main_page.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  late TextEditingController usernameController;
  late TextEditingController passwordController;
  late ScrollController scrollController;

  @override
  void initState() {
    usernameController = TextEditingController();
    passwordController = TextEditingController();
    scrollController = ScrollController();

    super.initState();
  }

  void registerButtonPressed() {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => const RegisterScreen()),
    );
  }

  void logInButtonPressed(String username, String password) async {

  if(passwordController.text.isEmpty || usernameController.text.isEmpty) {
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
  
  bool result = await Authentication.loginUser(username, password);

  if (result) {
      Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => const MainScreen()),
      );
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
            controller: usernameController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'Username',
            ),
          ),
        ),
        Container(
          padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
          child: TextField(
            obscureText: true,
            controller: passwordController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'Password',
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
              child: const Text('Log In'),
              onPressed: () => logInButtonPressed(
                  usernameController.text, passwordController.text),
            )),
        ElevatedButton(
          style: ElevatedButton.styleFrom(
                minimumSize: const Size.fromHeight(50),
              ),
          onPressed: () => registerButtonPressed(),
          child: const Text('Register'),
          ),
      ],
    ))));
  }
}
