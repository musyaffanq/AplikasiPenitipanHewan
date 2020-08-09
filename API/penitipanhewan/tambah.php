<?php
	//Import File Koneksi database
	require_once('koneksi.php');
	$target_path = dirname(__FILE__).'/uploads/';
	$_BASE_URL = 'http://192.168.1.10/penitipanhewan/uploads/';

	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		//Mendapatkan Nilai Variable
		$name = $_POST['name'];
		$telepon = $_POST['telepon'];
		$hewan= $_POST['hewan'];

		$target_path = $target_path . basename($_FILES['image']['name']);

		try{
			if(!move_uploaded_file($_FILES['image']['tmp_name'], $target_path)){
				echo json_encode(array('status'=>'KO', 'message'=> 'Image gagal diupload'));
			}
			else {
				$sql = "INSERT INTO tb_penitip (nama, telepon, hewan, image) VALUES ('$name','$telepon','$hewan','".$_BASE_URL.basename($_FILES['image']['name'])."')";

				//Eksekusi Query database
				if (mysqli_query($con, $sql)) {
					echo json_encode(array('status' => 'OK', 'message' => 'Berhasil Menambahkan Penitip'));
				} 
				else {
					echo json_encode(array('status' => 'KO', 'message' => 'Gagal Menambahkan Penitip'));
				}
				mysqli_close($con);
			}

		} 
		catch(Exception $e){
			echo json_encode(array('status'=>'KO', 'message'=> $e->getMessage()));
		}
		
	}
?>
