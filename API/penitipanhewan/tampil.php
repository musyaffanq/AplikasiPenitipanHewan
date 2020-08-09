<?php
	//Mendapatkan Nilai Dari Variable ID Penitip yang ingin ditampilkan
	$id = $_GET['id'];
	
	//Importing database
	require_once('koneksi.php');
	
	//Membuat SQL Query dengan penitip yang ditentukan secara spesifik sesuai ID
	$sql = "SELECT * FROM tb_penitip WHERE id=$id";
	
	//Mendapatkan Hasil 
	$r = mysqli_query($con,$sql);
	
	//Memasukkan Hasil Kedalam Array
	$result = array();
	$row = mysqli_fetch_array($r);
	array_push($result,array(
		"id"=>$row['id'],
		"name"=>$row['nama'],
		"telepon"=>$row['telepon'],
		"hewan"=>$row['hewan'],
		"image"=>$row['image']
		));
 
	//Menampilkan dalam format JSON
	echo json_encode($result[0]);
	
	mysqli_close($con);
?>