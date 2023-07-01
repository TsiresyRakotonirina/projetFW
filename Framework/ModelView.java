package etu002015.framework;

public class ModelView {

        String url;
        

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
    
}