<?php
  include_once("config.php");
  $id = $_POST['id'];

  $query = "SELECT * FROM ngaythang WHERE id = '$id'";
  $ketqua = mysqli_query($conn,$query);
  $chuoijson = array();
  echo "{";
  echo "\"ngaythang\":";
  if($ketqua){
    while ($dong=mysqli_fetch_array($ketqua)) {
      //Cách 1
      // $chuoijson[] = $dong;

      //Cách 2
      array_push($chuoijson, array("id"=>$dong["id"],
      "tuan"=>$dong["tuan"],
      "dayofyear"=>$dong["dayofyear"],
      "dayofweek"=>$dong["dayofweek"]));
    }
    echo json_encode($chuoijson);
  }
  echo "}";
 ?>
