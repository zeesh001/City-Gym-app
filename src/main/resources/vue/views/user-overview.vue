<template id="user-overview">
  <app-layout>
    <!-- Card - for adding a new user -->
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Users
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

        <form id="addUser">

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">Id</span>
            </div>
            <input type="number" class="form-control" v-model="formData.id" name="id" placeholder="Id"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="formData.name" name="name" placeholder="Name"/>
          </div>



          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-email">Email</span>
            </div>
            <input type="email" class="form-control" v-model="formData.email" name="email" placeholder="Email"/>
          </div>



          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-phone">Phone</span>
            </div>
            <input type="number" class="form-control" v-model="formData.phone" name="phone" placeholder="Phone"/>
          </div>


          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-address">address</span>
            </div>
            <input type="text" class="form-control" v-model="formData.address" name="address" placeholder="Address"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-gender">Gender</span>
            </div>
            <input type="text" class="form-control" v-model="formData.gender" name="name" placeholder="Gender"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-timing_slot">Timing Slot</span>
            </div>
            <input type="text" class="form-control" v-model="formData.timing_slot" name="timing_slot" placeholder="Timing Slot"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-trainer">Trainer</span>
            </div>
            <input type="text" class="form-control" v-model="formData.trainer" name="trainer" placeholder="Trainer"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-service_name">Service Name</span>
            </div>
            <input type="text" class="form-control" v-model="formData.service_name" name="service_name" placeholder="Service Name"/>
          </div>


        </form>

        <button rel="tooltip" title="Insert" class="btn btn-info btn-simple btn-link" @click="addUser()">Add User</button>
      </div>
    </div>

    <!-- List Group - displays all the users -->
    <div class="list-group list-group-flush">
      <table>
        <tr>
          <th>&nbsp &nbsp &nbsp</th>
          <th>Id </th>
          <th> &nbsp&nbsp &nbsp &nbsp &nbsp </th>
          <th>Name </th>
          <th> &nbsp &nbsp   &nbsp &nbsp &nbsp</th>
          <th>Email  </th>
          <th> &nbsp &nbsp &nbsp  &nbsp  &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp</th>
          <th>Phone  </th>
          <th> &nbsp &nbsp  &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp</th>
          <th>Gender  </th>
          <th> &nbsp  &nbsp &nbsp</th>
          <th>Adddress  </th>
          <th> &nbsp &nbsp &nbsp  &nbsp &nbsp &nbsp</th>
          <th>Trainer  </th>
          <th> &nbsp &nbsp   &nbsp &nbsp &nbsp &nbsp</th>
          <th>Training slot</th>
          <th> &nbsp &nbsp  &nbsp&nbsp &nbsp &nbsp &nbsp &nbsp</th>
          <th>Service  </th>
        </tr>
      </table>
      <div class="list-group-item d-flex align-items-start"
           v-for="(user,index) in users" v-bind:key="index">


        <div class="mr-auto p-2">
          <table style="width: 50px">
            <tr > {{ user.id }}</tr>
            <tr>&nbsp&nbsp &nbsp &nbsp &nbsp  </tr>

            <tr > {{ user.name }}</tr>
            <tr> &nbsp &nbsp &nbsp &nbsp</tr>

            <tr > {{ user.email }}</tr>
            <tr>  &nbsp &nbsp &nbsp &nbsp &nbsp</tr>
            <tr> {{ user.phone }}</tr>
            <tr>  &nbsp &nbsp &nbsp &nbsp &nbsp </tr>
            <tr> {{ user.gender }}</tr>
            <tr>  &nbsp &nbsp  &nbsp &nbsp &nbsp </tr>
            <tr> {{ user.address }}</tr>
            <tr>  &nbsp &nbsp &nbsp  &nbsp &nbsp &nbsp </tr>
            <tr> {{ user.trainer }}</tr>
            <tr>  &nbsp &nbsp &nbsp  &nbsp &nbsp &nbsp </tr>
            <tr> {{ user.timing_slot }}</tr>
            <tr>  &nbsp &nbsp &nbsp &nbsp  &nbsp &nbsp &nbsp  &nbsp &nbsp &nbsp </tr>
            <tr> {{ user.service_name }}</tr>


          </table>
        </div>



        <div class="p2">
          <a :href="`/users/${user.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deleteUser(user, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
    </div>
  </app-layout>
</template>



<script>
Vue.component("user-overview", {
  template: "#user-overview",
  data: () => ({
    users: [],
    formData: [],
    hideForm :true,
  }),
  created() {
    this.fetchUsers();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/users")
          .then(res => this.users = res.data)
          .catch(() => alert("Error while fetching users"));
    },
    deleteUser: function (user, index) {
      if (confirm('Are you sure you want to delete this user? This action cannot be undone.', 'Warning')) {
        //user confirmed delete
        const userId = user.id;
        const url = `/api/users/${userId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.users.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addUser: function (){
      const url = `/api/users`;
      axios.post(url,
          {
            id: this.formData.id,
            name: this.formData.name,
            email: this.formData.email,
            phone: this.formData.phone,
            address: this.formData.address,
            gender: this.formData.gender,
            timing_slot: this.formData.timing_slot,
            trainer: this.formData.trainer,
            service_name: this.formData.service_name
          })
          .then(response => {
            this.users.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>
