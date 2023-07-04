package etu002015.framework;

import java.util.HashMap;

public class ModelView {

        String url;
        HashMap<String, Object> data = new HashMap<String, Object>();

        public ModelView(String url){
            this.url=url;
        }

        public ModelView() {
        }
        
        public String getUrl(){
            return this.url;
        }
        public void setUrl(String url){
            this.url = url;
        }

        public HashMap<String, Object> getData(){
            return this.data;
        }
        // public void setData(HashMap<String, Object> data){
        //     this.data = data;
        // }

        public void addItem(String key, Object value){
            this.getData().put(key, value);
        }
        
    
}