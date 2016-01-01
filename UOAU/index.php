<?
$version = 0.7;
$fileDir = 'Files';

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
}