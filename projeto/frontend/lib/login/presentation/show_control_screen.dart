import 'package:adc_handson_session/login/application/auth.dart';
import 'package:flutter/material.dart';

class ShowControlsScreen extends StatefulWidget {
  const ShowControlsScreen({super.key});

  @override
  State<ShowControlsScreen> createState() => _ShowControlsScreenState();
}

class _ShowControlsScreenState extends State<ShowControlsScreen> {
  late ScrollController scrollController;
  Map<String, dynamic> controls = {};


  @override
  void initState() {
    scrollController = ScrollController();
    getControls();

    super.initState();
  }

  void getControls() async {
    controls = await Authentication.showControls();
    setState(() {
      controls = controls;
    });
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
          
              Text("Role: ${controls.entries.elementAt(0).value ?? ''}"),
              Text("State: ${controls.entries.elementAt(1).value ?? ''}"),
              Text("Token: ${controls.entries.elementAt(2).value ?? ''}"),
        ],
            ),
        )
      )
    );
          }
  }
