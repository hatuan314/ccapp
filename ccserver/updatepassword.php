<?php
  include "config.php";
    $manhanvien = $_POST['id'];
    $password = $_POST['password'];
    if(strlen($manhanvien)>0 && strlen($password)){
      $query = "UPDATE nhanvien SET password='$password' WHERE id='$manhanvien'";
      if(mysqli_query($conn, $query)){
        echo '1';
      } else {
        echo "thất bại";
      }
    } else {
      echo "Bạn hãy kiểm tra lại các dữ liệu";
    }
 ?>
