<?php
 //Mendapatkan Nilai ID
 $id = $_GET['id'];
 
 //Import File Koneksi Database
 require_once('koneksi.php');
 
 //Membuat SQL Query
 $sql = "DELETE FROM tb_penitip WHERE id=$id;";
 
//Menghapus Nilai pada Database 
if (mysqli_query($con, $sql)) {
	echo json_encode(array('status' => 'OK', 'message' => 'Berhasil Menghapus Penitip'));
} else {
	echo json_encode(array('status' => 'KO', 'message' => 'Gagal Menghapus Penitip'));
}

 mysqli_close($con);
 ?>