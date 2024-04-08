import 'package:flutter/material.dart';
import 'package:adc_handson_session/login/application/auth.dart';
import 'package:adc_handson_session/login/presentation/main_page.dart';

class RemoveUserScreen extends StatefulWidget {
  const RemoveUserScreen({super.key});

  @override
  State<RemoveUserScreen> createState() => _RemoveUserScreenState();
}

class _RemoveUserScreenState extends State<RemoveUserScreen> {
  late TextEditingController targetController;
  late ScrollController scrollController;

  @override
  void initState() {
    targetController = TextEditingController();
    scrollController = ScrollController();

    super.initState();
  }

  void RemoveUserButtonPressed(String target) async {

  if(targetController.text.isEmpty) {
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
  
  bool result = await Authentication.removeUser(target);

  if (result) {

      Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => const MainScreen()),
      );

    String success = "User removed succesfully!";
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
            controller: targetController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'Target username*',
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
              onPressed: () => RemoveUserButtonPressed(
                  targetController.text),
            )),
      ],
    ))));
  }
}
