<?php
//Mendefinisikan Konstanta
define('HOST', 'localhost');
define('USER', 'root');
define('PASS', '');
define('DB', 'penitipan_hewan');

//membuat koneksi dengan database
$con = mysqli_connect(HOST, USER, PASS, DB) or die('Unable to Connect');
?>