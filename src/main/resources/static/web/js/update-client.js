var app = new Vue({
    el:"#app",
    data:{
        company: "",
        maritalStatus: "",
        address: "",
        phone: "",
        clientInfo: {},
        errorToats: null,
        errorMsg: null,
    },
    methods:{
        getData: function(){
            axios.get("/api/clients/current")
                .then((response) => {
                    //get client ifo
                    this.clientInfo = response.data;
                    //this.phone = clientInfo.phone;
                    //this.address = clientInfo.address;
                })
                .catch((error)=>{
                    // handle error
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        checkApplication: function(){
            if(this.phone.length <= 0){
                this.errorMsg = "You must indicate a phone";
                this.errorToats.show();
            }
            else if(this.address.length <= 0){
                this.errorMsg = "You must indicate an address";
                this.errorToats.show();
            }
            else if(this.maritalStatus.length <= 0){
                this.errorMsg = "You must indicate a marital status";
                this.errorToats.show();
            }
            else if(this.company.length <= 0){
                this.errorMsg = "You must indicate a company";
                this.errorToats.show();
            }else{
                this.modal.show();
            }
        },
        update: function(id){
            event.preventDefault();
            let config = {
                headers: {
                    'content-type': 'application/x-www-form-urlencoded'
                }
            }
            axios.post('/api/clients/update?id='+id+'&phone='+this.phone+'&address='+this.address+'&maritalStatus='+this.maritalStatus+'&company='+this.company,config)
                .then(response => {
                    this.modal.hide();
                    this.okmodal.show();
                })
                .catch((error) =>{
                    this.errorMsg = error.response.data;
                    this.errorToats.show();
                })
        },
        finish: function(){
            window.location.reload();
        },
        signOut: function(){
            axios.post('/api/logout')
                .then(response => window.location.href="/web/index.html")
                .catch(() =>{
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        },
        enable: function(){
            var input = document.getElementById("phone"),
                input2 = document.getElementById("address"),
                input3 = document.getElementById("maritalStatus"),
                input4 = document.getElementById("company"),
                btnApply = document.getElementById("btnApply"),
                btnReset = document.getElementById("btnReset"),
                myBtn = document.getElementById("create");

            myBtn.addEventListener("click", function(){
                input.disabled = false;
                input2.disabled = false;
                input3.disabled = false;
                input4.disabled = false;
                btnApply.hidden = false;
                btnReset.hidden = false;
            }, false);
        }
    },
    mounted: function(){
            this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
            this.modal = new bootstrap.Modal(document.getElementById('confirModal'));
            this.okmodal = new bootstrap.Modal(document.getElementById('okModal'));
            this.getData();
    }
})