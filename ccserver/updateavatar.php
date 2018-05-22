<?php
  include "config.php";
    $manhanvien = $_POST['id'];
    $anhdaidien = $_POST['anhdaidien'];
    if(strlen($manhanvien)>0 && strlen($anhdaidien)){
      $query = "UPDATE nhanvien SET anhdaidien='$anhdaidien' WHERE id='$manhanvien'";
      if(mysqli_query($conn, $query)){
        echo '1';
      } else {
        echo "thất bại";
      }
    } else {
      echo "Bạn hãy kiểm tra lại các dữ liệu";
    }
 ?>
