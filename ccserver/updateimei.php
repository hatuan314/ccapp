<?php
  include "config.php";
    $manhanvien = $_POST['id'];
    $imei = $_POST['IMEI'];
    if(strlen($manhanvien)>0 && strlen($imei)){
      $query = "UPDATE nhanvien SET IMEI='$imei' WHERE id='$manhanvien'";
      if(mysqli_query($conn, $query)){
        echo '1';
      } else {
        echo "thất bại";
      }
    } else {
      echo "Bạn hãy kiểm tra lại các dữ liệu";
    }
 ?>
