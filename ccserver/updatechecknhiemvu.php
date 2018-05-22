<?php
  include "config.php";
    $id = $_POST['id'];
    $checknhiemvu = $_POST['checknhiemvu'];
    if(strlen($id)>0 && strlen($checknhiemvu)){
      $query = "UPDATE nhiemvu SET checknhiemvu='$checknhiemvu' WHERE id='$id'";
      if(mysqli_query($conn, $query)){
        echo '1';
      } else {
        echo "thất bại";
      }
    } else {
      echo "Bạn hãy kiểm tra lại các dữ liệu";
    }
 ?>
