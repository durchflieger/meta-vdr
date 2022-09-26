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

SRC_URI = "git://github.com/kfb77/vdr-plugin-markad;protocol=https;branch=V03 \
  file://02-fix-sscanf-getline-handling.patch \
"

#SRCREV="v${PV}"
SRCREV="4a77f7f030634ea2c2e693248ec4247f940f9ac5"

S = "${WORKDIR}/git"

inherit gettext

DEPENDS = "pkgconfig vdr ffmpeg"

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
