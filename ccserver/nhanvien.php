<?php
  include_once("config.php");
  $idnhanvien = '1';
  $query = "SELECT * FROM nhanvien WHERE id = '$idnhanvien'";
  $ketqua = mysqli_query($conn,$query);
  $chuoijson = array();
  echo "{";
  echo "\"nhanvien\":";
  if($ketqua){
    while ($dong=mysqli_fetch_array($ketqua)) {
      //Cách 1
      // $chuoijson[] = $dong;

      //Cách 2
      array_push($chuoijson, array("id"=>$dong["id"],
      "tendangnhap"=>$dong["tendangnhap"],
      "password"=>$dong["password"],
      "email"=>$dong["email"],
      "tennhanvien"=>$dong["tennhanvien"],
      "sodienthoai"=>$dong["sodienthoai"],
      "ngaysinh"=>$dong["ngaysinh"],
      "quequan"=>$dong["quequan"],
      "phongban"=>$dong["phongban"],
      "chucvu"=>$dong["chucvu"],
      "IMEI"=>$dong["IMEI"],
      "anhdaidien"=>$dong["anhdaidien"],
      "anhbia"=>$dong["anhbia"]));
    }
    echo json_encode($chuoijson);
  }
  echo "}";
 ?>
