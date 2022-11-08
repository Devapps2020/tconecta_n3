package com.blm.qiubopay.builder;

import com.blm.qiubopay.models.recarga.PaqueteDTO;
import com.blm.qiubopay.models.services.ServicePackageItemModel;
import com.blm.qiubopay.models.services.ServicePackageModel;
import com.blm.qiubopay.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ServiceItemsBuilder {

    public List<ServicePackageItemModel> getServiceItemListFromStrings(List<String> montosList){
        List<ServicePackageItemModel> list = new ArrayList<>();
        for(String monto : montosList){
            list.add(getServiceItemFrom(monto));
        }
        return list;
    }

    public List<ServicePackageItemModel> getServiceItemListFromPaquetes(List<PaqueteDTO> paquetesList, boolean setDescription){
        List<ServicePackageItemModel> list = new ArrayList<>();
        for(PaqueteDTO paqueteDTO : paquetesList){
            list.add(getServiceItemFrom(paqueteDTO, setDescription));
        }
        return list;
    }

    public List<ServicePackageItemModel> getServiceItemListFromNetweyResponse(List<ServicePackageModel> services){
        ArrayList<ServicePackageItemModel> listItems = new ArrayList<>();
        for(ServicePackageModel item : services){
            listItems.add(getServiceItemFrom(item));
        }

        return listItems;
    }

    public ServicePackageItemModel getServiceItemFrom(String monto){
        ServicePackageItemModel item = new ServicePackageItemModel();
        item.setAmount(monto);
        return item;
    }

    public ServicePackageItemModel getServiceItemFrom(PaqueteDTO paqueteDTO, boolean setDescription){
        ServicePackageItemModel item = new ServicePackageItemModel();
        item.setId(paqueteDTO.getIdOffer().toString());
        item.setAmount(Utils.paserCurrency(paqueteDTO.getAmount().toString()).replace(".00",""));
        if(setDescription) { item.setDescription(paqueteDTO.getDescriptionOffer()); }
        return item;
    }

    public ServicePackageItemModel getServiceItemFrom(ServicePackageModel model){
        ServicePackageItemModel item = new ServicePackageItemModel();
        item.setDescription(model.getTitle() + "\n" + model.getDescription());
        item.setAmount(Utils.paserCurrency(model.getPrice()));
        item.setId(model.getId());
        return item;
    }


}
