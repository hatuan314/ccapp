<?php
  include "config.php";
    $nhiemvu = $_POST['nhiemvu'];
    $checknhiemvu = $_POST['checknhiemvu'];
    $tuantrongnam = $_POST['tuantrongnam'];
    $ngaytrongtuan = $_POST['ngaytrongtuan'];
    $ngaytrongthang = $_POST['ngaytrongthang'];
    $idnhanvien = $_POST['idnhanvien'];
    if(strlen($nhiemvu)>0 && strlen($checknhiemvu) && strlen($tuantrongnam)>0 && strlen($ngaytrongtuan)>0 && strlen($ngaytrongthang)>0 && strlen($idnhanvien)){
      $query = "INSERT INTO nhiemvu(id,nhiemvu,checknhiemvu,tuantrongnam,ngaytrongtuan,ngaytrongthang,idnhanvien) VALUES (null,'$nhiemvu','$checknhiemvu','$tuantrongnam','$ngaytrongtuan','$ngaytrongthang','$idnhanvien')";
      if(mysqli_query($conn, $query)){
        echo '1';
      } else {
        echo "thất bại";
      }
    } else {
      echo "Bạn hãy kiểm tra lại các dữ liệu";
    }
 ?>
