package Services;

import Contracts.StorageInterface;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import org.slf4j.Logger;


@Service
public class StorageService implements StorageInterface {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ItemService itemService;

    private String storageLocation;

    @Autowired
    private ResourceLoader resourceLoader;

    private final Logger LOGGER = LoggerFactory.getLogger("Storage");

    private static String OS = System.getProperty("os.name").toLowerCase();
    
    @Override
    public StorageService store(MultipartFile file) throws Exception{
        LOGGER.info(OS.toString());
        if (file.isEmpty()){
            throw new Exception("Failed to store empty file ");
        }
        URI uri = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
        storageLocation = uri.getPath() + "storage/";
        String fileName = file.getOriginalFilename();
        Files.copy(file.getInputStream() , Paths.get(storageLocation+fileName), StandardCopyOption.REPLACE_EXISTING);
        if (Files.exists(Paths.get(storageLocation+fileName))){
            LOGGER.info("File have been saved");
        }
        return this;
    }

    public Resource[] getAllCsvFiles() throws IOException{
        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                .getResources(storageLocation+"/*.csv");
    }

    public void loadItemData(MultipartFile file){
        try{
            List<Map<String,String>> items = formatData(file);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
    }

    List<Map<String,String>> formatData(MultipartFile file) throws Exception{

        Map<String,String> dataObject = new HashMap<String,String>();
        List<Map<String,String>> listDataObjects = new ArrayList<>();
        if (file.isEmpty()){
            throw new Exception("Failed to read empty file ");
        }
        BufferedReader bufferedReaderCsvFile = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        int i = 0;
        String[] header = new String[0];
        String[] columns;
        while ((line = bufferedReaderCsvFile.readLine()) != null){
            if (i == 0){
                header = line.toLowerCase().split(",");
            }else{
                columns = line.split(",");
                for (int x =0 ; x < header.length ; x++){
                    if (x < columns.length){
                        dataObject.put(header[x] , columns[x]);
                    }
                }
                listDataObjects.add(new HashMap<String, String>(dataObject));
                dataObject.clear();
            }
            i++;
        }
        return listDataObjects;
    }
}
