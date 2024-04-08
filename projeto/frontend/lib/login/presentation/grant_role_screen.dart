import 'package:flutter/material.dart';
import 'package:adc_handson_session/login/application/auth.dart';
import 'package:adc_handson_session/login/presentation/main_page.dart';

class GrantRoleScreen extends StatefulWidget {
  const GrantRoleScreen({super.key});

  @override
  State<GrantRoleScreen> createState() => _GrantRoleScreenState();
}

class _GrantRoleScreenState extends State<GrantRoleScreen> {
  late TextEditingController targetController;
  late TextEditingController roleController;
  late ScrollController scrollController;

  @override
  void initState() {
    targetController = TextEditingController();
    roleController = TextEditingController();
    scrollController = ScrollController();

    super.initState();
  }

  void grantButtonPressed(String target, String role) async {

  if(targetController.text.isEmpty || roleController.text.isEmpty) {
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
  
  bool result = await Authentication.grantRole(target, role);

  if (result) {

      Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => const MainScreen()),
      );

    String success = "Role granted successfully!";
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
          padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
          child: TextField(
            controller: roleController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'Role*',
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
              onPressed: () => grantButtonPressed(
                  targetController.text, roleController.text),
            )),
      ],
    ))));
  }
}
