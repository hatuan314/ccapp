<?php
  include_once("config.php");
  $idnhanvien = $_POST['idnhanvien'];
  $query = "SELECT * FROM nhiemvu WHERE idnhanvien = '$idnhanvien'";
  $ketqua = mysqli_query($conn,$query);
  $chuoijson = array();
  echo "{";
  echo "\"nhiemvu\":";
  if($ketqua){
    while ($dong=mysqli_fetch_array($ketqua)) {
      //Cách 1
      // $chuoijson[] = $dong;

      //Cách 2
      array_push($chuoijson, array("id"=>$dong["id"],
      "nhiemvu"=>$dong["nhiemvu"],
      "checknhiemvu"=>$dong["checknhiemvu"],
      "tuantrongnam"=>$dong["tuantrongnam"],
      "ngaytrongtuan"=>$dong["ngaytrongtuan"],
      "ngaytrongthang"=>$dong["ngaytrongthang"],
      "idnhanvien"=>$dong["idnhanvien"]));
    }
    echo json_encode($chuoijson);
  }
  echo "}";
 ?>
