var app = new Vue({
    el:"#app",
    data:{
        clientAccounts: [],
        clientAccountsTo: [],
        debitCards: [],
        coordinates: ["A1","B1","C1","A2","B2","C2","A3","B3","C3"],
        coordinatesValues: [],
        colRowOne: 0,
        colRowTwo: 0,
        colRowThree: 0,
        coorOne: null,
        coorTwo: 0,
        coorThree: 0,
        errorToats: null,
        errorMsg: null,
        accountFromNumber: "VIN",
        accountToNumber: "VIN",
        trasnferType: "own",
        amount: 0,
        description: ""
    },
    methods:{
        getData: function(){
            axios.get("/api/clients/current/accounts")
            .then((response) => {
                //get client ifo
                this.clientAccounts = response.data;
                //this.coordinatesValues = this.clientInfo.coordinates;
                this.colRowOne = Math.floor(Math.random() * 8);
                this.colRowTwo = Math.floor(Math.random() * 8);
                this.colRowThree = Math.floor(Math.random() * 8);
            })
            .catch((error) => {
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })

             axios.get("/api/clients/current")
                        .then((response) => {
                            //get client ifo
                            this.coordinatesValues = response.data.coordinates;
                        })
                        .catch((error) => {
                            this.errorMsg = "Error getting data";
                            this.errorToats.show();
                        })
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        checkTransfer: function(){
            if(this.accountFromNumber == "VIN"){
                this.errorMsg = "You must select an origin account";  
                this.errorToats.show();
            }
            else if(this.accountToNumber == "VIN"){
                this.errorMsg = "You must select a destination account";  
                this.errorToats.show();
            }else if(this.amount == 0){
                this.errorMsg = "You must indicate an amount";  
                this.errorToats.show();
            }
            else if(this.description.length <= 0){
                this.errorMsg = "You must indicate a description";  
                this.errorToats.show();
            }
            else if(this.coorOne == ""){
                this.errorMsg = "You must indicate a coordinates";
                this.errorToats.show();
            }
            else if(this.coorTwo == ""){
                this.errorMsg = "You must indicate a coordinates";
                this.errorToats.show();
            }
            else if(this.coorThree == ""){
                this.errorMsg = "You must indicate a coordinates";
                this.errorToats.show();
            }
            else if(this.coorOne != this.coordinatesValues[this.colRowOne]){
                this.errorMsg = "Coordinates wrong";
                this.errorToats.show();
            }
            else if(this.coorTwo != this.coordinatesValues[this.colRowTwo]){
                this.errorMsg = "Coordinates wrong";
                this.errorToats.show();
            }
            else if(this.coorThree != this.coordinatesValues[this.colRowThree]){
                this.errorMsg = "Coordinates wrong";
                this.errorToats.show();
            }

            else{
                this.modal.show();
            }
        },
        transfer: function(){
            let config = {
                headers: {
                    'content-type': 'application/x-www-form-urlencoded'
                }
            }
            axios.post(`/api/transactions?fromAccountNumber=${this.accountFromNumber}&toAccountNumber=${this.accountToNumber}&amount=${this.amount}&description=${this.description}`,config)
            .then(response => { 
                this.modal.hide();
                this.okmodal.show();
            })
            .catch((error) =>{
                this.errorMsg = error.response.data;  
                this.errorToats.show();
            })
        },
        changedType: function(){
            this.accountFromNumber = "VIN";
            this.accountToNumber = "VIN";
        },
        changedFrom: function(){
            if(this.trasnferType == "own"){
                this.clientAccountsTo = this.clientAccounts.filter(account => account.number != this.accountFromNumber);
                this.accountToNumber = "VIN";
            }
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
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.modal = new bootstrap.Modal(document.getElementById('confirModal'));
        this.okmodal = new bootstrap.Modal(document.getElementById('okModal'));
        this.getData();
    }
})