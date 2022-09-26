# meta-vdr layer providing VDR for OE-Linux
This is the meta-vdr layer providing VDR for OE-Linux 5.0

## How to build the packages

The following build instructions uses OpenATV V7.0 as base system.

- Clone this repository using a directory outside the openatv clone/build directory.

- Install the OpenATV build system according to [OpenATV build-system](https://github.com/openatv/enigma2). \
Make sure you are using branch `7.0`.\
Follow the build instructions until including step `11. Update build-enviroment`. \

- For step 12. choose `Build specific packages` executing `make init` using a appropriate value for MACHINE for your STB. \
cd into the build directory.

- Edit file `conf/bblayers.conf` and add path of cloned meta-vdr directory to the value of variable BBLAYERS.

- Use `bitbake` to build packages of meta-vdr e.g. `bitbake vdr`.

- To build all packages at once call `build.sh` script in meta-vdr.

- You will find the installable opkg packages in the directory tree of under `tmp/deploy/ipk`.

