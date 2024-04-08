import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:email_validator/email_validator.dart';
import 'package:shared_preferences/shared_preferences.dart';

class Authentication {
  static bool isEmailCompliant(String email) {
    return EmailValidator.validate(email);
  }

  static bool isPasswordCompliant(String password, [int minLength = 6]) {
    if (password.isEmpty) {
      return false;
    }

/*
    bool hasUppercase = password.contains(RegExp(r'[A-Z]'));
    bool hasDigits = password.contains(RegExp(r'[0-9]'));
    bool hasLowercase = password.contains(RegExp(r'[a-z]'));
    bool hasSpecialCharacters =
        password.contains(RegExp(r'[!@#$%^&*(),.?":{}|<>]'));
    bool hasMinLength = password.length > minLength;
*/
    return true; 
        //hasDigits &
        //hasUppercase &
        //hasLowercase &
        //hasSpecialCharacters &
        //hasMinLength;
  }

  static Future<bool> loginUser(String username, String password) async {

    final response = await http.post(
    Uri.parse('https://melodic-subject-415914.ew.r.appspot.com/rest/login/user'),
    headers: <String, String>{
      'Content-Type': 'application/json',
    },
    body: jsonEncode(<String, String>{
      'username': username,
      'password': password,
    }),
    );

    if (response.statusCode == 200) {
      Map<String, dynamic> token = jsonDecode(response.body);
      saveToken( token["username"], token['tokenID'], token["creationData"], token["expirationData"]);
      return true;

    } else {
      final SharedPreferences prefs = await SharedPreferences.getInstance();
      prefs.setString('message', response.body);
      return false;

    }
  }

  static Future<bool> registerRoot() async {
    final response = await http.post(
      Uri.parse('https://melodic-subject-415914.ew.r.appspot.com/rest/register/su'),
      headers: <String, String>{
        'Content-Type': 'application/json',
      },
      body: jsonEncode(<String, String> {
      }),
    );

    if (response.statusCode == 200) {
      return true;

    } else {
      final SharedPreferences prefs = await SharedPreferences.getInstance();
      prefs.setString('message', response.body);
      return false;
    }
  }

  static Future<bool> registerUser(String username, String email, String name, String phoneNumber, String password, 
  String confirmation, String profile, String ocupation, String workplace, String address, String zipCode, String taxNumber) async {

    final response = await http.post(
      Uri.parse('https://melodic-subject-415914.ew.r.appspot.com/rest/register/user'),
      headers: <String, String>{
        'Content-Type': 'application/json',
      },
      body: jsonEncode(<String, String>{
        'username': username,
        'email': email,
        'name': name,
        'phoneNumber': phoneNumber,
        'password': password,
        'confirmation': confirmation,
        'profile': profile,
        'ocupation': ocupation,
        'workplace': workplace,
        'address': address,
        'zipCode': zipCode,
        'taxNumber': taxNumber,
      }),
    );

    if (response.statusCode == 200) {
      return true;

    } else {
      final SharedPreferences prefs = await SharedPreferences.getInstance();
      prefs.setString('message', response.body);
      return false;
    }
  }

  static Future<bool> grantRole(String target, String role) async {
    String username = await getUsername();
    String tokenID = await getTokenID();
    int creationData = await getCreationData();
    int expirationData = await getExpirationData();

    final response = await http.post(
      Uri.parse('https://melodic-subject-415914.ew.r.appspot.com/rest/grant/role'),
      headers: <String, String>{
        'Content-Type': 'application/json',
      },
      body: jsonEncode(<String, dynamic>{
        'targetUsername': target,
        'role': role,
        'token': {
          'username': username,
          'tokenID': tokenID,
          'creationData': creationData,
          'expirationData': expirationData,
        },
      }),
    );

    if (response.statusCode == 200) {
      return true;

    } else {
      final SharedPreferences prefs = await SharedPreferences.getInstance();
      prefs.setString('message', response.body);
      return false;
    }
  }

  static Future<bool> grantState(String target, String state) async {
    String username = await getUsername();
    String tokenID = await getTokenID();
    int creationData = await getCreationData();
    int expirationData = await getExpirationData();

    final response = await http.post(
      Uri.parse('https://melodic-subject-415914.ew.r.appspot.com/rest/grant/state'),
      headers: <String, String>{
        'Content-Type': 'application/json',
      },
      body: jsonEncode(<String, dynamic>{
        'targetUsername': target,
        'state': state,
        'token': {
          'username': username,
          'tokenID': tokenID,
          'creationData': creationData,
          'expirationData': expirationData,
        },
      }),
    );

    if (response.statusCode == 200) {
      return true;

    } else {
      final SharedPreferences prefs = await SharedPreferences.getInstance();
      prefs.setString('message', response.body);
      return false;
    }
  }

  static Future<bool> changePassword(String oldPassword, String newPassword, String confirmation) async {
    String username = await getUsername();
    String tokenID = await getTokenID();
    int creationData = await getCreationData();
    int expirationData = await getExpirationData();

    final response = await http.post(
      Uri.parse('https://melodic-subject-415914.ew.r.appspot.com/rest/new/password'),
      headers: <String, String>{
        'Content-Type': 'application/json',
      },
      body: jsonEncode(<String, dynamic>{
        'oldPassword': oldPassword,
        'newPassword': newPassword,
        'confirmation': confirmation,
        'token': {
          'username': username,
          'tokenID': tokenID,
          'creationData': creationData,
          'expirationData': expirationData,
        },
      }),
    );

    if (response.statusCode == 200) {
      return true;

    } else {
      final SharedPreferences prefs = await SharedPreferences.getInstance();
      prefs.setString('message', response.body);
      return false;
    }
  }

  static Future<bool> removeUser(String target) async {
    String username = await getUsername();
    String tokenID = await getTokenID();
    int creationData = await getCreationData();
    int expirationData = await getExpirationData();

    final response = await http.post(
      Uri.parse('https://melodic-subject-415914.ew.r.appspot.com/rest/remove/user'),
      headers: <String, String>{
        'Content-Type': 'application/json',
      },
      body: jsonEncode(<String, dynamic>{
        'targetUsername': target,
        'token': {
          'username': username,
          'tokenID': tokenID,
          'creationData': creationData,
          'expirationData': expirationData,
        },
      }),
    );

    if (response.statusCode == 200) {
      return true;

    } else {
      final SharedPreferences prefs = await SharedPreferences.getInstance();
      prefs.setString('message', response.body);
      return false;
    }
  }

  static Future<bool> modifyAttrib(String target, String? email, String name, String phoneNumber, String password, 
  String profile, String ocupation, String workplace, String address, String zipCode, String taxNumber) async {
    String username = await getUsername();
    String tokenID = await getTokenID();
    int creationData = await getCreationData();
    int expirationData = await getExpirationData();

    final response = await http.post(
      Uri.parse('https://melodic-subject-415914.ew.r.appspot.com/rest/modify/attributes'),
      headers: <String, String>{
        'Content-Type': 'application/json',
      },
      body: jsonEncode(<String, dynamic>{
        'targetUsername': target,
        'email': email,
        'name': name,
        'phoneNumber': phoneNumber,
        'password': password,
        'profile': profile,
        'ocupation': ocupation,
        'workplace': workplace,
        'address': address,
        'zipCode': zipCode,
        'taxNumber': taxNumber,
        'token': {
          'username': username,
          'tokenID': tokenID,
          'creationData': creationData,
          'expirationData': expirationData,
        },
      }),
    );

    if (response.statusCode == 200) {
      return true;

    } else {
      final SharedPreferences prefs = await SharedPreferences.getInstance();
      prefs.setString('message', response.body);
      return false;
    }
  }

  static Future<List<dynamic>> listUsers() async {
    String username = await getUsername();
    String tokenID = await getTokenID();
    int creationData = await getCreationData();
    int expirationData = await getExpirationData();

    final response = await http.post(
      Uri.parse('https://melodic-subject-415914.ew.r.appspot.com/rest/list/users'),
      headers: <String, String>{
        'Content-Type': 'application/json',
      },
      body: jsonEncode(<String, dynamic>{
        'token': {
          'username': username,
          'tokenID': tokenID,
          'creationData': creationData,
          'expirationData': expirationData,
        },
      }),
    );

    final SharedPreferences prefs = await SharedPreferences.getInstance();
    if (response.statusCode == 200) {
      return jsonDecode(response.body);

    } else {
      prefs.setString('message', response.body);
      return [];
    }
  }

  static Future<Map<String, dynamic>> showControls() async {
    String username = await getUsername();
    String tokenID = await getTokenID();
    int creationData = await getCreationData();
    int expirationData = await getExpirationData();

    final response = await http.post(
      Uri.parse('https://melodic-subject-415914.ew.r.appspot.com/rest/show/controlAttributes'),
      headers: <String, String>{
        'Content-Type': 'application/json',
      },
      body: jsonEncode(<String, dynamic>{
        'token': {
          'username': username,
          'tokenID': tokenID,
          'creationData': creationData,
          'expirationData': expirationData,
        },
      }),
    );

    final SharedPreferences prefs = await SharedPreferences.getInstance();
    if (response.statusCode == 200) {
      return jsonDecode(response.body);

    } else {
      prefs.setString('message', response.body);
      return {};
    }
  }

  static Future<void> logout() async {
    deleteToken();
  }

  static Future<void> saveToken(String username, String tokenID, int creationData, int expirationData) async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setString('username', username);
    prefs.setString('tokenID', tokenID);
    prefs.setInt('creationData', creationData);
    prefs.setInt('expirationData', expirationData);
  }

  static Future<String> getToken() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getString('token') ?? '';
  }

  static Future<String> getUsername() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getString('username') ?? '';
  }

  static Future<String> getTokenID() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getString('tokenID') ?? '';
  }

  static Future<int> getCreationData() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getInt('creationData') ?? 0;
  }

  static Future<int> getExpirationData() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    return  prefs.getInt('expirationData') ?? 0;
  }

  static Future<void> deleteToken() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.remove('username');
    prefs.remove('tokenID');
    prefs.remove('creationData');
    prefs.remove('expirationData');
  }

  static Future<String> getMessage() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getString('message') ?? '';
  }
}

void main() async {
}
