package BewareData;

/**
 * Created by Prasanna on 08-10-2015.
 */

public class District {

        private String value;
        private String label;

        public District(){}

        public District(String value, String label){
            this.value = value;
            this.label = label;
        }

        public String getValue(){
            return this.value;
        }

        public void setValue(String value){
            this.value = value;
        }

        public void setLabel(String label){
            this.label = label;
        }

        public String getLabel(){
            return this.label;
        }

    }

