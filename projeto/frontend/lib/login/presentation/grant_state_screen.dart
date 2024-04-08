import 'package:flutter/material.dart';
import 'package:adc_handson_session/login/application/auth.dart';
import 'package:adc_handson_session/login/presentation/main_page.dart';

class GrantStateScreen extends StatefulWidget {
  const GrantStateScreen({super.key});

  @override
  State<GrantStateScreen> createState() => _GrantStateScreenState();
}

class _GrantStateScreenState extends State<GrantStateScreen> {
  late TextEditingController targetController;
  late TextEditingController stateController;
  late ScrollController scrollController;

  @override
  void initState() {
    targetController = TextEditingController();
    stateController = TextEditingController();
    scrollController = ScrollController();

    super.initState();
  }

  void grantButtonPressed(String target, String state) async {

  if(targetController.text.isEmpty || stateController.text.isEmpty) {
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
  
  bool result = await Authentication.grantState(target, state);

  if (result) {

      Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => const MainScreen()),
      );

    String success = "state granted successfully!";
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
            controller: stateController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'State*',
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
                  targetController.text, stateController.text),
            )),
      ],
    ))));
  }
}
