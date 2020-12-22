import 'dart:async';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:path_provider/path_provider.dart';



class TextStorageApplicationSupportDirectory {
  Future<String> get _localPath async {
    final directory = await  getTemporaryDirectory();


    return directory.path;
  }
 
  Future<File> get _localFile async {
    final path = await _localPath;
    return File('$path/text.txt');
  }
 
  Future<String> readFile() async {
    try {
      final file = await _localFile;
 
      String content = await file.readAsString();
      return content;
    } catch (e) {
      return '';
    }
  }
 
  Future<File> writeFile(String text) async {
    final file = await
