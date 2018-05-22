<?php
  include_once("config.php");
  $page = $_GET['page'];
  $space = 10;
  $limit = ($page -1)*$space;
  $query = "SELECT * FROM thongbao LIMIT $limit,$space";
  $ketqua = mysqli_query($conn,$query);
  $chuoijson = array();
  echo "{";
  echo "\"thongbao\":";
  if($ketqua){
    while ($dong=mysqli_fetch_array($ketqua)) {
      //Cách 1
      // $chuoijson[] = $dong;

      //Cách 2
      array_push($chuoijson, array("id"=>$dong["id"],
      "thoigian"=>$dong["thoigian"],
      "tieude"=>$dong["tieude"],
      "noidung"=>$dong["noidung"]));
    }
    echo json_encode($chuoijson);
  }
  echo "}";
 ?>
