import 'package:flutter/material.dart';
import 'package:adc_handson_session/login/application/auth.dart';
import 'package:adc_handson_session/login/presentation/main_page.dart';

class ModifyAttribScreen extends StatefulWidget {
  const ModifyAttribScreen({super.key});

  @override
  State<ModifyAttribScreen> createState() => _ModifyAttribState();
}

class _ModifyAttribState extends State<ModifyAttribScreen> {
  late TextEditingController targetController;
  late TextEditingController emailController;
  late TextEditingController nameController;
  late TextEditingController phoneNumberController;
  late TextEditingController passwordController;
  late TextEditingController profileController;
  late TextEditingController ocupationController;
  late TextEditingController workplaceController;
  late TextEditingController addressController;
  late TextEditingController zipCodeController;
  late TextEditingController taxNumberController;
  late ScrollController scrollController;

@override
  void initState() {
    targetController = TextEditingController();
    emailController = TextEditingController();
    nameController = TextEditingController();
    phoneNumberController = TextEditingController();
    passwordController = TextEditingController();
    profileController = TextEditingController();
    ocupationController = TextEditingController();
    workplaceController = TextEditingController();
    addressController = TextEditingController();
    zipCodeController = TextEditingController();
    taxNumberController = TextEditingController();
    scrollController = ScrollController();

    super.initState();
  }

void modifyAttribButtonPressed(String username, String email, String name, String phoneNumber, String password,
    String profile, String ocupation, String workplace, String address, String zipCode, String taxNumber) async {

    bool result = await Authentication.modifyAttrib(username, email, name, phoneNumber, password, profile, ocupation, 
    workplace, address, zipCode, taxNumber);

    if(result) {

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
      child: Column(
        children: [
        Container(
          padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
          child: TextField(
            controller: targetController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'Target Username',
            ),
          ),
        ),
        Container(
          padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
          child: TextField(
            controller: emailController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'Email',
            ),
          ),
        ),
        Container(
          padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
          child: TextField(
            controller: nameController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'Name',
            ),
          ),
        ),
        Container(
          padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
          child: TextField(
            controller: phoneNumberController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'Phone Number',
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
          padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
          child: TextField(
            controller: profileController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'Visibility',
            ),
          ),
        ),
        Container(
          padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
          child: TextField(
            controller: ocupationController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'Ocupation',
            ),
          ),
        ),
        Container(
          padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
          child: TextField(
            controller: workplaceController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'Workplace',
            ),
          ),
        ),
        Container(
          padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
          child: TextField(
            controller: addressController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'Address',
            ),
          ),
        ),
        Container(
          padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
          child: TextField(
            controller: zipCodeController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'Zip Code',
            ),
          ),
        ),
        Container(
          padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
          child: TextField(
            controller: taxNumberController,
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(90.0),
              ),
              labelText: 'Tax Number',
            ),
          ),
        ),
        Container(
          padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
          child: ElevatedButton(
            onPressed: () => modifyAttribButtonPressed(
            targetController.text, emailController.text, nameController.text, phoneNumberController.text, passwordController.text,
            profileController.text, ocupationController.text, workplaceController.text, addressController.text, zipCodeController.text, taxNumberController.text),
            child: const Text('Modify Attributes!'),
          ),
        ),
        ]),
    )));
  }

}