<?
$version = 0.7;
$fileDir = 'Files'; //This actually can't be changed yet or it will break the updater.
$offer_full_client = true;
$full_client = "UO7.0.15.1.zip"; //This must be in a different directory than Files, or in the same directory as this index.php file


if(!isset($_GET['data']))
	die();
$data = $_GET['data'];

switch($data){
	case "updates":
		$json = array(
		'kiru_version' => $version
		);
		echo json_encode($json);
		break;
	case "files":
		$files = scandir($fileDir);
		$filesFinal = array();
		
		foreach($files as $f){
			if($f == ".")
				continue;
			if($f == "..")
				continue;
			if(is_file($fileDir . "/" . $f)){
				$tFinal = array(
				'filename' => $f,
				'size' => filesize($fileDir . "/" . $f),
				'hash' => md5_file($fileDir . "/" . $f)
				);
				$filesFinal[$f] = $tFinal;
			}
		}
		echo json_encode(array("files" => array_values($filesFinal)));
		break;
	case "offerfullclient":
		if($offer_full_client)
			echo "true";
		else
			echo "false";

		break;
	case "fullclienturl":
		echo json_encode(array(
		'file_size' => filesize($full_client),
		'file' => $full_client
		));
		break;
}