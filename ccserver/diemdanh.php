<?php
  include "config.php";
    $idnhanvien = $_POST['idnhanvien'];
    $tenhanvien = $_POST['tenhanvien'];
    $ngay = $_POST['ngay'];
    $gio = $_POST['thoigian'];
    $checkdiemdanh = $_POST['checkdiemdanh'];
    if(strlen($idnhanvien)>0 && strlen($tenhanvien) && strlen($ngay)>0 && strlen($gio)>0 && strlen($checkdiemdanh)>0){
      $query = "INSERT INTO diemdanh(id,idnhanvien,tenhanvien,ngay,thoigian,checkdiemdanh) VALUES (null,'$idnhanvien','$tenhanvien','$ngay','$gio','$checkdiemdanh')";
      if(mysqli_query($conn, $query)){
        echo '1';
      } else {
        echo "thất bại";
      }
    } else {
      echo "Bạn hãy kiểm tra lại các dữ liệu";
    }
 ?>
