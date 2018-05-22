<?php
  include_once("config.php");
  $idnhanvien = $_POST['idnhanvien'];

  $query = "SELECT * FROM cauhoibaomat WHERE idnhanvien = '$idnhanvien'";
  $ketqua = mysqli_query($conn,$query);
  $chuoijson = array();
  echo "{";
  echo "\"cauhoibaomat\":";
  if($ketqua){
    while ($dong=mysqli_fetch_array($ketqua)) {
      //Cách 1
      // $chuoijson[] = $dong;

      //Cách 2
      array_push($chuoijson, array("id"=>$dong["id"],
      "cauhoi"=>$dong["cauhoi"],
      "cautraloi"=>$dong["cautraloi"],
      "idnhanvien"=>$dong["idnhanvien"]));
    }
    echo json_encode($chuoijson);
  }
  echo "}";
 ?>
