PR = "r0"
MAINTAINER = "df"
SECTION = "multimedia"

PLUGIN_BASE_NAME = "${@d.getVar('PN').replace('vdr-plugin-', '')}"
PLUGIN_CONF_FILE = "50-${PLUGIN_BASE_NAME}.conf"

SUMMARY = "Plugin for VDR to mark advertisements in recordings"

DESCRIPTION = "This plugin for the Linux Video Disc Recorder VDR will search for and mark advertisements in VDR recordings."

HOMEPAGE = "https://github.com/kfb77/vdr-plugin-markad"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d34f68dad6fc14cfc1e9eec76fa9964"

SRC_URI = "git://github.com/kfb77/vdr-plugin-markad;protocol=https;branch=master"

SRCREV="146b24bcfa8ccba2cbb0d8e5d90ac5abb1cfdf25"

S = "${WORKDIR}/git"

inherit gettext pkgconfig

DEPENDS = "vdr ffmpeg"

PACKAGES = "${PN}-dbg ${PN} ${PN}-doc"

RDEPENDS:${PN} =+ "vdr"

FILES:${PN} =+ "${libdir}/vdr/plugins"
FILES:${PN} =+ "${datadir}/locale"
FILES:${PN} =+ "${sysconfdir}/vdr/conf.d/${PLUGIN_CONF_FILE}"
FILES:${PN} =+ "${localstatedir}/lib/${PLUGIN_BASE_NAME}"

CONFFILES:${PN} =+ "${sysconfdir}/vdr/conf.d/${PLUGIN_CONF_FILE}"

do_install () {
  oe_runmake install DESTDIR=${D}

  install -D -m 0644 -t ${D}${docdir}/${PN} ${S}/README
  install -D -m 0644 -t ${D}${docdir}/${PN} ${S}/INSTALL

  install -d -m 0755 ${D}${localstatedir}/lib/${PLUGIN_BASE_NAME}

  echo "[${PLUGIN_BASE_NAME}]" > ${WORKDIR}/${PLUGIN_CONF_FILE}
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/conf.d ${WORKDIR}/${PLUGIN_CONF_FILE}
}
