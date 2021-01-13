<template id="service-overview">
  <app-layout>


    <!-- Card - for adding a new user -->
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Service
          </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm =!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body" :class="{ 'd-none': hideForm}">

        <form id="addService">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">Id</span>
            </div>
            <input type="text" class="form-control" v-model="formData.id" name="id" placeholder="Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-service_name">Service Name</span>
            </div>
            <input type="text" class="form-control" v-model="formData.service_name" name="service_name" placeholder="Service Name"/>
          </div>
        </form>
        <button rel="tooltip" title="Add" class="btn btn-info btn-simple btn-link" @click="addService()">Add Service</button>
      </div>
    </div>



    <!-- List Group - displays all the users -->
    <div class="list-group list-group-flush">
<table>
  <tr>
    <th>&nbsp &nbsp &nbsp </th>
    <th>Id </th>
    <th> &nbsp &nbsp  &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp <span>|</span> &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp</th>
    <th>Services </th>
    <th> &nbsp &nbsp  &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp <span>|</span> &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp</th>
    <th>Enrolled Users  </th>

  </tr>
</table>
      <div class="list-group-item d-flex align-items-start"
           v-for="(service,index) in services" v-bind:key="index">
        <div class="mr-auto p-2">
          <table style="width: 50px">
            <tr > {{ service.id }}</tr>
            <tr>  &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp </tr>

            <tr > {{ service.service_name }}</tr>
            <tr>  &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp </tr>
            <tr> {{ service.enrolled_user }}</tr>
          </table>
        </div>

        <div class="p2">
          <a :href="`/services/${service.service_name}/services`">
            <button rel="tooltip" title="Review" class="btn btn-info btn-simple btn-link">
              <i class="far fa-address-book"></i>
            </button>
          </a>

<!--          <a :href="`/services/${service.service_name}/services`">-->
<!--            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">-->
<!--              <i class="fa fa-pencil" aria-hidden="true"></i>-->
<!--            </button>-->
<!--          </a>-->

          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deleteUser(service, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("service-overview", {
  template: "#service-overview",
  data: () => ({
    services: [],
    formData: [],
    hideForm :true,
  }),
  created() {
    this.fetchUsers();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/services")
          .then(res => this.services = res.data)
          .catch(() => alert("Error while fetching users"));
    },
    deleteUser: function (service, index) {
      if (confirm('Are you sure you want to delete this user? This action cannot be undone.', 'Warning')) {
        //user confirmed delete
        const serv = service.service_name;
        const url = `/api/services/${serv}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.services.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addService: function (){
      const url = `/api/services`;
      axios.post(url,
          {
            service_name: this.formData.service_name,
            id: this.formData.id
          })
          .then(response => {
            this.services.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>
