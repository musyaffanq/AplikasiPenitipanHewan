<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
		//MEndapatkan Nilai Dari Variable 
		$id = $_POST['id'];
		$name = $_POST['name'];
		$telepon = $_POST['telepon'];
		$hewan = $_POST['hewan'];
		
		//import file koneksi database 
		require_once('koneksi.php');
		
		//Membuat SQL Query
		$sql = "UPDATE tb_penitip SET nama = '$name', telepon = '$telepon', hewan = '$hewan' WHERE id = $id;";
		
		//Meng-update Database
		if (mysqli_query($con, $sql)) {
			echo json_encode(array('status' => 'OK', 'message' => 'Berhasil Update Data Penitip'));
		} else {
			echo json_encode(array('status' => 'KO', 'message' => 'Gagal Update Data Penitip'));
		}
		
		mysqli_close($con);
	}
?>