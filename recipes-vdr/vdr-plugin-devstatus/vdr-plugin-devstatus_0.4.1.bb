PR = "r0"
MAINTAINER = "df"
SECTION = "multimedia"

PLUGIN_BASE_NAME = "${@d.getVar('PN').replace('vdr-plugin-', '')}"
PLUGIN_CONF_FILE = "50-${PLUGIN_BASE_NAME}.conf"

SUMMARY = "Show status of all video devices used by VDR"

DESCRIPTION = "This plugin for the Video Disk Recorder shows the status of all video devices used by VDR."

HOMEPAGE = "http://www.u32.de/vdr.html"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"

SRC_URI = "http://www.u32.de/download/vdr-devstatus-${PV}.tgz \
  file://10-recordingfound.patch \
  file://15-channelswitch.patch \
  file://20-Makefile.patch \
  file://25-dvb-adapter-frontend.patch \
  file://30-memoryleak.patch \
  file://35-dynamite.patch \
  file://40-vdr-2.3.2-devstatus-0.4.1.patch \
"

SRC_URI[sha256sum] = "333acc400802db4470d16e85f0e56eeca50d9523ddf6c716381b45d17acbebaf"

S = "${WORKDIR}/${PLUGIN_BASE_NAME}-${PV}"

inherit gettext pkgconfig

DEPENDS = "vdr"

PACKAGES = "${PN}-dbg ${PN} ${PN}-doc"

RDEPENDS:${PN} =+ "vdr"

FILES:${PN} =+ "${libdir}/vdr/plugins"
FILES:${PN} =+ "${datadir}/locale"

CONFFILES:${PN} =+ "${sysconfdir}/vdr/conf.d/${PLUGIN_CONF_FILE}"

do_install () {
  oe_runmake install DESTDIR=${D}

  install -D -m 0644 -t ${D}${docdir}/${PN} ${S}/README

  echo "[${PLUGIN_BASE_NAME}]" > ${WORKDIR}/${PLUGIN_CONF_FILE}
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/conf.d ${WORKDIR}/${PLUGIN_CONF_FILE}
}
