var app = new Vue({
    el:"#app",
    data:{
        clientInfo: {},
        card: {},
        coordinates: [],
        errorToats: null,
        errorMsg: null,
    },
    methods:{
        getData: function(){
        const urlParams = new URLSearchParams(window.location.search);
                    const id = urlParams.get('id');
            axios.get("/api/clients/current")
            .then((response) => {
                //get client ifo
                this.clientInfo = response.data;
                this.card = this.clientInfo.cards.filter(card => card.id == id);
                this.coordinates = this.card.getCoordinates;
            })
            .catch((error) => {
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
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
        this.getData();
    }
})