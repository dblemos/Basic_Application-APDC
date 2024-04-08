import 'package:adc_handson_session/login/application/auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class ListUsersScreen extends StatefulWidget {
  const ListUsersScreen({super.key});

  @override
  State<ListUsersScreen> createState() => _ListUsersScreenState();
}

class _ListUsersScreenState extends State<ListUsersScreen> {
  List<dynamic> users = [];
  late ScrollController scrollController;
  int? size;


  @override
  void initState() {
    scrollController = ScrollController();
    getUsers();
    super.initState();
  }

  void getUsers() async {
    users = await Authentication.listUsers();
    setState(() {
      users = users;
    });
    size = users.length;
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
          ListView.builder(
            shrinkWrap: true,
            scrollDirection: Axis.vertical,
            itemCount: size ?? 0,
            itemBuilder: (BuildContext context, int index) {
              return ListTile(
                tileColor: (index % 2 == 0) ? (Colors.blue[100]) : (Colors.blue[200]),
                title:
            Column(children: [
              Text("Username: ${users[index]['username'] ?? ''}"),
              Text("Email: ${users[index]['email'] ?? ''}"),
              Text("Name: ${users[index]['name'] ?? ''}"),
              Text("Phone Number: ${users[index]['phoneNumber'] ?? ''}"),
              Text("Profile Visibility: ${users[index]['profile'] ?? ''}"),
              Text("Ocupation: ${users[index]['ocupation'] ?? ''}"),
              Text("Workplace: ${users[index]['workplace'] ?? ''}"),
              Text("Address: ${users[index]['address'] ?? ''}"),
              Text("Zip Code: ${users[index]['zipCode'] ?? ''}"),
              Text("Tax Number: ${users[index]['taxNumber'] ?? ''}"),
            ],),
            horizontalTitleGap: 50,
            );
          },
        ),
        ],
    ))));
  }
}
